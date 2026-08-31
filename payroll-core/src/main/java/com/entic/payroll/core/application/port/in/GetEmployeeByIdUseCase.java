package com.entic.payroll.core.application.port.in;

import com.entic.payroll.core.application.commons.operation.ApplicationUseCase;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseOut;

public interface GetEmployeeByIdUseCase extends ApplicationUseCase<GetEmployeeByIdUseCaseIn, GetEmployeeByIdUseCaseOut> {
}