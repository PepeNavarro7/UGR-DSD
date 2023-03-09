#include "calculadora.h"


void calculadora_1(char *host){
	CLIENT *clnt;
	double  *result_1;
	double suma_1_arg1;
	double suma_1_arg2;
	double  *result_2;
	double resta_1_arg1;
	double resta_1_arg2;
	double  *result_3;
	double multiplica_1_arg1;
	double multiplica_1_arg2;
	double  *result_4;
	double divide_1_arg1;
	double divide_1_arg2;

#ifndef	DEBUG
	clnt = clnt_create (host, CALCULADORA, BASICA, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result_1 = suma_1(suma_1_arg1, suma_1_arg2, clnt);
	if (result_1 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_2 = resta_1(resta_1_arg1, resta_1_arg2, clnt);
	if (result_2 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_3 = multiplica_1(multiplica_1_arg1, multiplica_1_arg2, clnt);
	if (result_3 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_4 = divide_1(divide_1_arg1, divide_1_arg2, clnt);
	if (result_4 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	printf("Hola");
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int main (int argc, char *argv[]){
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	printf("h");
	host = argv[1];
	calculadora_1 (host);
exit (0);
}
