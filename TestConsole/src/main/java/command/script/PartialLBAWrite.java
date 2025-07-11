package command.script;

import command.common.Command;
import command.common.CommandResult;
import command.common.ConsoleService;

//2_PartialLBAWrite
public class PartialLBAWrite extends TestScript {
    public static final int LOOP_COUNT = 30;
    public static final String TEST_VALUE = "0x12345678";
    public static final int[] LBA_TEST_SEQUENCE = new int[]{4, 0, 3, 1, 2};

    public PartialLBAWrite(ConsoleService service) {
        super(service);
    }

    @Override
    public CommandResult doExecute(String[] args) {
        for (int i = 0; i < LOOP_COUNT; i++) {
            for (int lba : LBA_TEST_SEQUENCE) {
                service.write(lba, TEST_VALUE);
            }

            for (int lba : LBA_TEST_SEQUENCE) {
                if (!service.readCompare(lba, TEST_VALUE)) {
                    return CommandResult.scriptFail(FAIL_FLAG);
                }
            }
        }

        return CommandResult.pass("PASS");
    }
}
