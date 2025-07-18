package command.script;

import command.common.*;
import logger.Logger;
import logger.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ScriptRunner implements RunMode {
    private final String filePath;

    public ScriptRunner(String filePath) {
        this.filePath = filePath;
    }

    Logger logger = Logger.getInstance();

    public void run() {
        if (!fileExists(filePath)) {
            String message = "There's no File !";
            logger.info(message);
            return;
        }

        ConsoleService service = new ConsoleService();
        CommandFactory factory = new CommandFactory();
        CommandInvoker invoker = new CommandInvoker();

        CommandRegistrar.registerScriptCommands(invoker, factory, service);

        List<String> scriptNames;
        try {
            scriptNames = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!isValidScriptNames(scriptNames, invoker)) {
            logger.info("Invalid Script Names!");
            return;
        }

        for (String script : scriptNames) {
            logger.result(script + "  ___  Run...", LoggerContext.CONSOLE_NO_NEWLINE);
            if (invoker.execute(script).shouldExit()) {
                return;
            }
        }
    }

    private boolean isValidScriptNames(List<String> scriptNames, CommandInvoker invoker) {
        if (scriptNames.isEmpty()) return false;

        int scriptCount = scriptNames.size();
        int validScriptCount = 0;
        for (String script : scriptNames) {
            if (invoker.hasCommand(script)) {
                validScriptCount++;
            }
        }

        return validScriptCount == scriptCount;
    }

    private boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}
