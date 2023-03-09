#include "calculadora.h"
#include <stdlib.h>


void calculadora_1(char *host, double x, char op, double  y){
	CLIENT *clnt;
	double  *result;
	double arg1 = x;
	double arg2 = y;


#ifndef	DEBUG
	clnt = clnt_create (host, CALCULADORA, BASICA, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */
	switch(op){
		case '+': result = suma_1(arg1, arg2, clnt); break;
		case '-': result = resta_1(arg1, arg2, clnt); break;
		case 'x': result = multiplica_1(arg1, arg2, clnt); break;
		case '/': result = divide_1(arg1, arg2, clnt); break;
		default: printf("No reconozco ese operador\n"); break;
	}
	if (result == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	} else {
		printf("Resultado: %f %c %f = %f\n",arg1,op,arg2,*result);
	}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int main (int argc, char *argv[]){
	char *host;

	if (argc != 5) {
		printf ("usage: %s server_host operacion\n", argv[0]); //localhost
		exit (1);
	}
	host = argv[1];
	double x = strtod(argv[2],NULL), y = strtod(argv[4],NULL);
	char op = *argv[3];
	printf("Operacion: %f %c %f\n",x,op,y);
	calculadora_1(host,x,op,y);
	exit (0);
}
