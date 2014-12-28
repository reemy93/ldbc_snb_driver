package com.ldbc.driver.validation;

import com.google.common.collect.Lists;
import com.ldbc.driver.Db;
import com.ldbc.driver.DbException;
import com.ldbc.driver.Workload;
import com.ldbc.driver.WorkloadException;
import com.ldbc.driver.control.ConsoleAndFileDriverConfiguration;
import com.ldbc.driver.control.DriverConfigurationException;
import com.ldbc.driver.generator.GeneratorFactory;
import com.ldbc.driver.generator.RandomDataGeneratorFactory;
import com.ldbc.driver.testutils.TestUtils;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery14;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery14Result;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcSnbInteractiveConfiguration;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcSnbInteractiveWorkload;
import com.ldbc.driver.workloads.ldbc.snb.interactive.db.DummyLdbcSnbInteractiveDb;
import com.ldbc.driver.workloads.ldbc.snb.interactive.db.DummyLdbcSnbInteractiveOperationInstances;
import com.ldbc.driver.workloads.ldbc.snb.interactive.db.DummyLdbcSnbInteractiveOperationResultInstances;
import com.ldbc.driver.workloads.ldbc.snb.interactive.db.DummyLdbcSnbInteractiveOperationResultSets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DbValidatorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldFailValidationWhenDbImplementationIsIncorrect() throws DbException, WorkloadException, IOException, DriverConfigurationException {
        // Given
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42l));
        List<ValidationParam> correctValidationParamsList = Lists.newArrayList(gf.limit(gf.repeating(buildParams().iterator()), 10000));

        LdbcQuery14 operation14 = DummyLdbcSnbInteractiveOperationInstances.read14();
        List<LdbcQuery14Result> unexpectedResult14 = DummyLdbcSnbInteractiveOperationResultSets.read14Results();
        unexpectedResult14.add(DummyLdbcSnbInteractiveOperationResultInstances.read14Result());

        ValidationParam unexpectedValidationParam14 = new ValidationParam(operation14, unexpectedResult14);
        correctValidationParamsList.add(unexpectedValidationParam14);

        Iterator<ValidationParam> validationParams = correctValidationParamsList.iterator();
        Db db = new DummyLdbcSnbInteractiveDb();
        db.init(new HashMap<String, String>());
        DbValidator dbValidator = new DbValidator();

        long operationCount = 1;
        String dbClassName = DummyLdbcSnbInteractiveDb.class.getName();
        String workloadClassName = LdbcSnbInteractiveWorkload.class.getName();
        ConsoleAndFileDriverConfiguration configuration = ConsoleAndFileDriverConfiguration.fromDefaults(dbClassName, workloadClassName, operationCount);

        Map<String, String> paramsMap = LdbcSnbInteractiveConfiguration.defaultReadOnlyConfig();
        paramsMap.put(LdbcSnbInteractiveConfiguration.PARAMETERS_DIRECTORY, TestUtils.getResource("/").getAbsolutePath());
        paramsMap.put(LdbcSnbInteractiveConfiguration.UPDATES_DIRECTORY, TestUtils.getResource("/").getAbsolutePath());
        configuration = (ConsoleAndFileDriverConfiguration) configuration.applyMap(paramsMap);

        Workload workload = new LdbcSnbInteractiveWorkload();
        workload.init(configuration);

        // When
        DbValidationResult validationResult = dbValidator.validate(validationParams, db);

        // Then
        System.out.println(validationResult.resultMessage());
        assertThat(validationResult.isSuccessful(), is(false));
    }

    @Test
    public void shouldPassValidationWhenDbImplementationIsCorrect() throws WorkloadException, DbException, IOException, DriverConfigurationException {
        // Given
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42l));
        Iterator<ValidationParam> validationParams = gf.limit(gf.repeating(buildParams().iterator()), 10000);
        Db db = new DummyLdbcSnbInteractiveDb();
        db.init(new HashMap<String, String>());
        DbValidator dbValidator = new DbValidator();

        long operationCount = 1;
        String dbClassName = DummyLdbcSnbInteractiveDb.class.getName();
        String workloadClassName = LdbcSnbInteractiveWorkload.class.getName();
        ConsoleAndFileDriverConfiguration configuration = ConsoleAndFileDriverConfiguration.fromDefaults(dbClassName, workloadClassName, operationCount);

        Map<String, String> paramsMap = LdbcSnbInteractiveConfiguration.defaultReadOnlyConfig();
        paramsMap.put(LdbcSnbInteractiveConfiguration.PARAMETERS_DIRECTORY, TestUtils.getResource("/").getAbsolutePath());
        paramsMap.put(LdbcSnbInteractiveConfiguration.UPDATES_DIRECTORY, TestUtils.getResource("/").getAbsolutePath());
        configuration = (ConsoleAndFileDriverConfiguration) configuration.applyMap(paramsMap);

        Workload workload = new LdbcSnbInteractiveWorkload();
        workload.init(configuration);

        // When
        DbValidationResult validationResult = dbValidator.validate(validationParams, db);

        // Then
        System.out.println(validationResult.resultMessage());
        assertThat(String.format("Validation Result\n%s", validationResult.resultMessage()),
                validationResult.isSuccessful(), is(true));
    }

    List<ValidationParam> buildParams() {
        ValidationParam validationParamLong1 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read1(),
                DummyLdbcSnbInteractiveOperationResultSets.read1Results()
        );

        ValidationParam validationParamLong2 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read2(),
                DummyLdbcSnbInteractiveOperationResultSets.read2Results()
        );

        ValidationParam validationParamLong3 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read3(),
                DummyLdbcSnbInteractiveOperationResultSets.read3Results()
        );

        ValidationParam validationParamLong4 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read4(),
                DummyLdbcSnbInteractiveOperationResultSets.read4Results()
        );

        ValidationParam validationParamLong5 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read5(),
                DummyLdbcSnbInteractiveOperationResultSets.read5Results()
        );

        ValidationParam validationParamLong6 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read6(),
                DummyLdbcSnbInteractiveOperationResultSets.read6Results()
        );

        ValidationParam validationParamLong7 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read7(),
                DummyLdbcSnbInteractiveOperationResultSets.read7Results()
        );

        ValidationParam validationParamLong8 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read8(),
                DummyLdbcSnbInteractiveOperationResultSets.read8Results()
        );

        ValidationParam validationParamLong9 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read9(),
                DummyLdbcSnbInteractiveOperationResultSets.read9Results()
        );

        ValidationParam validationParamLong10 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read10(),
                DummyLdbcSnbInteractiveOperationResultSets.read10Results()
        );

        ValidationParam validationParamLong11 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read11(),
                DummyLdbcSnbInteractiveOperationResultSets.read11Results()
        );

        ValidationParam validationParamLong12 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read12(),
                DummyLdbcSnbInteractiveOperationResultSets.read12Results()
        );

        ValidationParam validationParamLong13 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read13(),
                DummyLdbcSnbInteractiveOperationResultInstances.read13Result()
        );

        ValidationParam validationParamLong14 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.read14(),
                DummyLdbcSnbInteractiveOperationResultSets.read14Results()
        );

        ValidationParam validationParamShort1 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short1(),
                DummyLdbcSnbInteractiveOperationResultSets.short1Results()
        );

        ValidationParam validationParamShort2 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short2(),
                DummyLdbcSnbInteractiveOperationResultSets.short2Results()
        );

        ValidationParam validationParamShort3 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short3(),
                DummyLdbcSnbInteractiveOperationResultSets.short3Results()
        );

        ValidationParam validationParamShort4 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short4(),
                DummyLdbcSnbInteractiveOperationResultSets.short4Results()
        );

        ValidationParam validationParamShort5 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short5(),
                DummyLdbcSnbInteractiveOperationResultSets.short5Results()
        );

        ValidationParam validationParamShort6 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short6(),
                DummyLdbcSnbInteractiveOperationResultSets.short6Results()
        );

        ValidationParam validationParamShort7 = new ValidationParam(
                DummyLdbcSnbInteractiveOperationInstances.short7(),
                DummyLdbcSnbInteractiveOperationResultSets.short7Results()
        );

        return Lists.newArrayList(
                validationParamLong1,
                validationParamLong2,
                validationParamLong3,
                validationParamLong4,
                validationParamLong5,
                validationParamLong6,
                validationParamLong7,
                validationParamLong8,
                validationParamLong9,
                validationParamLong10,
                validationParamLong11,
                validationParamLong12,
                validationParamLong13,
                validationParamLong14,
                validationParamShort1,
                validationParamShort2,
                validationParamShort3,
                validationParamShort4,
                validationParamShort5,
                validationParamShort6,
                validationParamShort7
        );
    }
}
