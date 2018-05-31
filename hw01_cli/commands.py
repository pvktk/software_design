import subprocess
import string
import os
import re

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
        
        countArgs = 0
        
        nlines = 0
        if arglist.count('-A') > 0:
            idx = arglist.index('-A')
            snum = arglist[idx+1]
            if not snum.isdigit():
                return 'grep: parameter for -A option is invalid\n'
            nlines = int(snum)
            countArgs += 2
        
        onlyWholeWord = False
        if arglist.count('-w') > 0:
            onlyWholeWord = True
            countArgs += 1
        
        caseIns = False
        if arglist.count('-i') > 0:
            caseIns = True
            countArgs += 1
        
        lines = []
        pattern = ''
        if len(arglist) - countArgs > 2:
            return 'grep: arguments are invalid\n'
        
        if len(arglist) - countArgs == 2:
            pattern = arglist[-2]
            filename = arglist[-1]
            with open(filename, 'r') as f:
                lines = f.read().splitlines()
        
        if len(arglist) - countArgs == 1:
            pattern = arglist[-1]
            lines = pipe_arg.split('\n')
        
        if len(arglist) - countArgs < 1:
            return "grep: no pattern specified\n"
        
        if onlyWholeWord:
            pattern = r'\W' + pattern + r'\W'
        
        pattern = '.*' + pattern + '.*'
        if caseIns:
            regexp = re.compile(pattern, flags = re.IGNORECASE)
        else:
            regexp = re.compile(pattern)
        
        res = ''
        for i in range(len(lines)):
            if regexp.match(lines[i]):
                res += ('\n'.join(lines[i:i + nlines + 1])) + '\n'

        return res
