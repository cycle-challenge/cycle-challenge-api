package com.yeohangttukttak.api.global.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class SpatialFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
                .register("dwithin",
                        new StandardSQLFunction("st_dwithin", StandardBasicTypes.BOOLEAN));

        functionContributions.getFunctionRegistry()
                .register("distance_sphere",
                        new StandardSQLFunction("st_distancesphere", StandardBasicTypes.DOUBLE));
    }

}
