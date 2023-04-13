import glob
import sys

from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import logging

logging.basicConfig(level=logging.DEBUG)

# Aqui definimos las operaciones
class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def ping(self):
        print("me han hecho ping()")

    def suma(self, n1, n2):
        print(str(n1) + " + " + str(n2))
        return n1 + n2

    def resta(self, n1, n2):
        print(str(n1) + " - " + str(n2))
        return n1 - n2

    def multiplica(self, n1, n2):
        print(str(n1) + " x " + str(n2))
        return n1 * n2

    def divide(self, n1, n2):
        print(str(n1) + " / " + str(n2))
        return n1 / n2
    def modulo(self, n1, n2):
        print(str(n1) + " % " + str(n2))
        return n1 % n2
    def potencia(self, n1, n2):
        print(str(n1) + " elevado a " + str(n2))
        return pow(n1,n2)
    def raiz(self, n1, n2):
        print("raiz " + str(n1) + " de " + str(n2))
        #La raiz de un numero es igual al numero elevado al inverso de la raiz
        return pow(n2,(1.0/n1))


# Esta parte esta calcada de la version 0
if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")
