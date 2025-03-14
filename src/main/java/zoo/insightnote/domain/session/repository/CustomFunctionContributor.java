package zoo.insightnote.domain.session.repository;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;

public class CustomFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();

        // group_concat 함수 등록
//        functionRegistry.registerPattern(
//                "group_concat",
//                "group_concat(distinct ?1 separator ',')",
//                functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)
//        );

        functionRegistry.registerPattern(
                "group_concat",
                "group_concat(distinct ?1 separator ?2)", // separator를 파라미터로 처리
                functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)
        );
    }
}