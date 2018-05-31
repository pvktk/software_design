#!/usr/bin/python3

import subprocess
import string
import os

#Класс, вызывающий внешние комманды
class system_commands:
    def run(self, pipe_arg, command_name, arglist):
        #print('command for execute: ', [command_name] + arglist)
        proc = subprocess.Popen([command_name] + arglist, stdin=subprocess.PIPE, stdout=subprocess.PIPE)
        proc.stdin.write(pipe_arg.encode())
        proc.stdin.close()
        outpipe = proc.stdout.read().decode()
        proc.wait()
        return outpipe

#Классы для внутренних команд.
#У этих классов есть метод run_on, в который передаётся pipe в виде строки и
#аргументы в виде списка. Возвращает строку-результат

#Команда 'cat'
class cat_command:
    def run_on(self, pipe_arg, arglist):
        if len(arglist) > 0:
            with open(arglist[0], 'r') as f:
                s = f.readlines()
                return ''.join(s)
        else:
            return pipe_arg

#Команда 'echo'
class echo_command:
    def run_on(self, pipe_arg, arglist):
        return ' '.join(arglist) + '\n'

#Команда 'wc'
class wc_command:
    def run_on(self, pipe_arg, arglist):
        if len(arglist) > 0:
            with open(arglist[0], 'r') as f:
                s = f.readlines()
            nlines = len(s)
            s = ''.join(s)
            nbytes = len(s)
            nwords = len(s.split())
            return '{:2}{:3}{:3}'.format(nlines, nwords, nbytes) + ' ' + arglist[0] + '\n'
        else:
            nlines = len(pipe_arg.split('\n')) - 1
            nwords = len(pipe_arg.split())
            nbytes = len(pipe_arg)
            return '{:7}{:8}{:8}'.format(nlines, nwords, nbytes) + '\n'

#Команда 'pwd'
class pwd_command:
    def run_on(self, pipe_arg, arglist):
        return os.getcwd()+'\n'

#Команда 'exit'.
class exit_command:
    def __init__(self):
        pass
    def run_on(self, pipe_arg, arglist):
        raise Exception('exit')

#Команда 'cd'.
class cd_command:
    def __init__(self):
        pass
    def run_on(self, pipe_arg, arglist):
        os.chdir(arglist[0])
        return ""

#Команда 'ls'
class ls_command:
    def __init__(self):
        pass
    def run_on(self, pipe_arg, arglist):
        if len(arglist) == 0:
            a = os.listdir(".")
        else:
            a = os.listdir(arglist[0])
        a = [x for x in a if x[0] != '.']
        return "\n".join(a) + "\n"

#Класс, который исполняет выделенную команду с разбитыми на список аргументами
#Определяет, внешняя команда или внутренняя
class command_executer:
    def __init__(self):
        self.implemented_commands = {'cat' : cat_command(),
                                     'exit' : exit_command(),
                                     'echo' : echo_command(),
                                     'wc' : wc_command(),
                                     'pwd' : pwd_command(),
                                     'cd' : cd_command(),
                                     'ls' : ls_command()}
        self.sys_comm = system_commands()
#метод для исполния распарсенной строки. Возвращает результат в виде строки.
    def execute(self, pipe_arg, command_name, arglist):
        if command_name in self.implemented_commands:
            outpipe = self.implemented_commands[command_name].run_on(pipe_arg, arglist)
        else:
            outpipe = self.sys_comm.run(pipe_arg, command_name, arglist)
        return outpipe

#Класс для работы с объявлениями и подстановками переменных
class dollar_handler:
    def __init__(self):
        self.known_variables = {}

#Метод для подстановки переменных в строку с учетом кавычек.
#Возвращает строку с выполненным подстановками
    def replace_dollars(self, input_line):
        res = ''
        dollar_var_name = ''
        single_quote_opened = False
        dollar_opened = False
        for c in input_line:
            if single_quote_opened:
                if c == "'":
                    single_quote_opened = False
                res += c
                continue

            if dollar_opened:
                if c in (string.ascii_letters + string.digits):
                    dollar_var_name += c
                    continue
                else:
                    #print('dollar_var_name: ', dollar_var_name)
                    if dollar_var_name not in self.known_variables:
                        raise Exception('Неизвестная переменная ' + dollar_var_name )
                    res += self.known_variables[dollar_var_name]
                    dollar_var_name = ''
                    dollar_opened = False

            if c == "'":
                single_quote_opened = True
                res += c
                continue

            if c == '$':
                dollar_opened = True
                continue

            res += c
        if dollar_opened:
            if dollar_var_name not in self.known_variables:
                raise Exception('Неизвестная переменная ' + dollar_var_name )
            res += self.known_variables[dollar_var_name]

        return res

#Метод проверяет, не является ли строка объявлением переменной.
#Возвращает True, если найдено корректное объявление переменной
    def create_variable(self, input_line):
        var_name = ''
        for i in range(len(input_line)):
            if input_line[i] == '=':
                self.known_variables[var_name] = self.replace_dollars(input_line[i+1:])
                return True

            if input_line[i] in (string.ascii_letters + string.digits):
                var_name += input_line[i]
            else:
                return False


#Парсер
class parser:
    def __init__(self):
        pass

#Разбивает строку по пайпам, для каждого пайпа выдаёт список из команды и её аргументов
    def get_parsed_lists(self, input_line):
        res = [['']]
        single_quote_opened = False
        double_quote_opened = False
        dollar_opened = False

        dollar_var_name = ''

        for c in input_line:

            if single_quote_opened:
                if c == "'":
                    single_quote_opened = False
                    continue
                res[-1][-1] += c
                continue

            if double_quote_opened:
                if c == '"':
                    double_quote_opened = False
                    continue
                res[-1][-1] += c
                continue

            if c == "'":
                single_quote_opened = True
                continue
            if c == '"':
                double_quote_opened = True
                continue

            if c == ' ':
                if res[-1][-1] != '':
                    res[-1].append('')
                continue
            if c == '|':
                if res[-1] != ['']:
                    if res[-1][-1] == '':
                        res[-1].pop()
                    res.append([''])
                continue

            res[-1][-1] += c
        if res[-1][-1] == '':
            res[-1].pop()
        if single_quote_opened or double_quote_opened:
            raise Exception('Конец строки при открытых кавычках')
        return res

#Класс для вычисления результата исполнения всей строки
#Использует всё перечисленное выше.
class line_executer:
    def __init__(self):
        self.parser = parser()
        self.command_executer = command_executer()
        self.dollar_handler = dollar_handler()

#Основной метод класса. Получает на вход введённую строчку,
#возвращает результат её вычисления.
    def exec_line(self, inpline):
        if self.dollar_handler.create_variable(inpline):
            return ''
        try:
            inpline = self.dollar_handler.replace_dollars(inpline)
            parse_res = self.parser.get_parsed_lists(inpline)
        except Exception as e:
            return 'Ошибка в строке: '+str(e)+'\n'
        pipe = ''
        for i in parse_res:
            try:
                pipe = self.command_executer.execute(pipe, i[0], i[1:])
            except Exception as e:
                if str(e) == 'exit':
                    raise e
                else:
                    print(e)
                    return ''
        return pipe

#REPL
if __name__ == '__main__':
    lexec = line_executer()
    while (True):
        inpline = input('>#> ')
        try:
            print(lexec.exec_line(inpline), end='')
        except:
            exit()
