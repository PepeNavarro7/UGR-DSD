#include "calculadora.h"

double * suma_1_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1+arg2;
	return &result;
	printf("suma");
}

double * resta_1_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1-arg2;
	return &result;
}

double * multiplica_1_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1*arg2;
	return &result;
}

double * divide_1_svc(double arg1, double arg2,  struct svc_req *rqstp){
	static double  result;
	result = arg1/arg2;
	return &result;
}
