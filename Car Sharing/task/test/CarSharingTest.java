import carsharing.Main;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.io.File;

public class CarSharingTest extends StageTest<Void> {

    private static final String databaseFileName = "src/carsharing/db/carsharing.mv.db";
    private static DatabaseUtil db = new DatabaseUtil();

    @DynamicTestingMethod
    public CheckResult test1_ifDatabaseExist() {

        TestedProgram program = new TestedProgram(Main.class);
        program.start("-databaseFileName", "carsharing");

        if (!program.isFinished()) {
            return CheckResult.wrong("After starting your program should initialize the database, create table named 'company' and stop.");
        }

        File file = new File(databaseFileName);

        if (!file.exists()) {
            return CheckResult.wrong("Can't find a database file. It should be named 'carsharing.mv.db'" +
                    " and located in /carsharing/db/ folder.\n" +
                    "Note that .mv.db extension will be added automatically to the database file name.");
        }

        return correct();
    }

    @DynamicTestingMethod
    public CheckResult test2_checkDatabaseConnection() {
        db.getConnection();
        return correct();
    }

    @DynamicTestingMethod
    public CheckResult test3_checkIfTableExists() {
        if (!db.ifTableExist("COMPANY")) {
            return wrong("Can't find table named 'COMPANY'");
        }
        return correct();
    }

    @DynamicTestingMethod
    public CheckResult test4_checkTableColumns() {
        String[][] columns = {{"ID", "INT"}, {"NAME", "VARCHAR"}};
        db.ifColumnsExist("COMPANY", columns);
        return correct();
    }

    private CheckResult wrong(String message) {
        db.closeConnection();
        return CheckResult.wrong(message);
    }

    private CheckResult correct() {
        db.closeConnection();
        return CheckResult.correct();
    }


}
