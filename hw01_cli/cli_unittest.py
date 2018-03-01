#!/usr/bin/python3

from cli import line_executer
import unittest
import os

class SingleCommandsTest(unittest.TestCase):
    def setUp(self):
        self.le = line_executer()

    def test_echo(self):
        r = self.le.exec_line('echo 123')
        self.assertEqual(r, '123\n')
        
        r = self.le.exec_line('echo 123 456')
        self.assertEqual(r, '123 456\n')
        
        r = self.le.exec_line('echo 123 "456"')
        self.assertEqual(r, '123 456\n')
        
        r = self.le.exec_line('''echo 123 "'456'"''')
        self.assertEqual(r, "123 '456'\n")

    def test_cat(self):
        r = self.le.exec_line('cat example.txt')
        self.assertEqual(r, 'Some example\ntext\n')
        
        r = self.le.exec_line('cat "example.txt"')
        self.assertEqual(r, 'Some example\ntext\n')
        
        r = self.le.exec_line('echo 123 | cat')
        self.assertEqual(r, '123\n')

    def test_pwd(self):
        r = self.le.exec_line('pwd')
        self.assertEqual(r, os.getcwd()+'\n')
        
    def test_wc(self):
        r = self.le.exec_line('wc example.txt')
        self.assertEqual(r, ' 2  3 18 example.txt\n')
        
        r = self.le.exec_line("wc 'example.txt'")
        self.assertEqual(r, ' 2  3 18 example.txt\n')

        r = self.le.exec_line('cat example.txt | wc')
        self.assertEqual(r, '      2       3      18\n')

        r = self.le.exec_line('echo 123 | wc ')
        self.assertEqual(r, '      1       1       4\n')

class ComplexTests(unittest.TestCase):
    def setUp(self):
        self.le = line_executer()

    def test_vars(self):
        self.le.exec_line('x=5')
        self.le.exec_line('y=hello')
        
        r = self.le.exec_line('echo x')
        self.assertEqual(r, 'x\n')
        
        r = self.le.exec_line('echo $x')
        self.assertEqual(r, '5\n')
        
        r = self.le.exec_line('echo $x ')
        self.assertEqual(r, '5\n')
        
        r = self.le.exec_line('echo $y')
        self.assertEqual(r, 'hello\n')
        
        r = self.le.exec_line('echo "$y"')
        self.assertEqual(r, 'hello\n')
        
        r = self.le.exec_line('''echo "'$y'"''')
        self.assertEqual(r, "'$y'\n")
        
        r = self.le.exec_line('echo $x$y')
        self.assertEqual(r, "5hello\n")

        r = self.le.exec_line('echo $x $y ')
        self.assertEqual(r, "5 hello\n")
        
        r = self.le.exec_line('echo $xy ')
        self.assertEqual(r, "Ошибка в строке: Неизвестная переменная xy\n")
        
        self.le.exec_line('x=echo 123')
        r = self.le.exec_line('$x')
        self.assertEqual(r, "123\n")

        r = self.le.exec_line('echo $x')
        self.assertEqual(r, "echo 123\n")

    def test_pipes(self):
        r = self.le.exec_line('echo 123 | cat|cat | cat ')
        self.assertEqual(r, '123\n')

        r = self.le.exec_line('echo 123 | cat|cat | wc ')
        self.assertEqual(r, '      1       1       4\n')

    def test_external_command(self):
        r = self.le.exec_line('echo "ibase=2; 10" | bc')
        self.assertEqual(r, '2\n')

if __name__ == '__main__':
    unittest.main()

