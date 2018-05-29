import subprocess
import string
import os

"""
Класс, вызывающий внешние комманды
"""
class system_commands:
    def run(self, pipe_arg, command_name, arglist):
        proc = subprocess.Popen([command_name] + arglist, stdin=subprocess.PIPE, stdout=subprocess.PIPE)
        proc.stdin.write(pipe_arg.encode())
        proc.stdin.close()
        outpipe = proc.stdout.read().decode()
        proc.wait()
        return outpipe

"""
Классы для внутренних команд.
У этих классов есть метод run_on, в который передаётся pipe в виде строки и
аргументы в виде списка. Возвращает строку-результат
"""
"""
Команда 'cat'
"""
class cat_command:
    def run_on(self, pipe_arg, arglist):
        if len(arglist) > 0:
            with open(arglist[0], 'r') as f:
                s = f.readlines()
                return ''.join(s)
        else:
            return pipe_arg

"""
Команда 'echo'
"""
class echo_command:
    def run_on(self, pipe_arg, arglist):
        return ' '.join(arglist) + '\n'

"""
Команда 'wc'
"""
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

"""
Команда 'pwd'
"""
class pwd_command:
    def run_on(self, pipe_arg, arglist):
        return os.getcwd() + '\n'

"""
Команда 'exit'
"""
class exit_command:
    def __init__(self):
        pass
    def run_on(self, pipe_arg, arglist):
        raise Exception('exit')

"""
команда grep
"""
class grep_command:
    def run_on(self, pipe_arg, arglist):
        
