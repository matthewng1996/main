package wallet.logic.command;

import wallet.model.Wallet;
import wallet.storage.StorageManager;

/**
 * EditCommand Class deals with commands that involves
 * in editing a specific object
 * in the a specific list.
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";

    @Override
    public boolean execute(Wallet wallet, StorageManager storageManager) {
        return false;
    }
}