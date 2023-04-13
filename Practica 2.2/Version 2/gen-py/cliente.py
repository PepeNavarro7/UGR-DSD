from calculadora import Calculadora

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

#print("hacemos ping al server")
client.ping()

print("Introduce la operacion:")
teclado = input()
teclado = teclado.split()
x_f = float(teclado[0])
y_f = float(teclado[2])
op = teclado[1]


#En python3 introdujeron el match-case a modo de switch-case
match op:
    case '+': 
        resultado = client.suma(x_f,y_f)
    case '-': 
        resultado = client.resta(x_f,y_f)
    case 'x': 
        resultado = client.multiplica(x_f,y_f)
    case '/': 
        resultado = client.divide(x_f,y_f)
    case "mod": 
        x_i = int(teclado[0])
        y_i = int(teclado[2])
        resultado = client.modulo(x_i,y_i)
    case "pow": 
        y_i = int(teclado[2])
        resultado = client.potencia(x_f,y_i)
    case "root": 
        x_i = int(teclado[0])
        resultado = client.raiz(x_i,y_f)
    
print(resultado)

transport.close()
