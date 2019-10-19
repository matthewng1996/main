package wallet.logic.command;

import wallet.model.Wallet;
import wallet.model.contact.Contact;
import wallet.model.record.Expense;
import wallet.model.record.Loan;
import wallet.model.task.Task;
import wallet.ui.Ui;

/**
 * The AddCommand Class which extends Command.
 */
public class AddCommand extends Command {
    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_USAGE = "Usage for add command."
            + "\nThe options for recurrence rate (/r) are \\\"daily, weekly or monthly\\\""
            + "\nExample: " + COMMAND_WORD + "expense lunch $5 Food /on 20/02/2019"
            + "\nExample: " + COMMAND_WORD + "expense phone bills $100 bills /on 10/10/2019 /r monthly"
            + "\nExample: " + COMMAND_WORD + "expense dinner $10 Food";
    public static final String MESSAGE_SUCCESS_ADD_TASK = "Got it. I've added this task:";
    public static final String MESSAGE_SUCCESS_ADD_CONTACT = "Got it. I've added this contact:";
    public static final String MESSAGE_SUCCESS_ADD_EXPENSE = "Got it. I've added this expense:";
    public static final String MESSAGE_SUCCESS_ADD_LOAN = "Got it. I've added this loan:";
    public static final String MESSAGE_ERROR_ADD_EXPENSE = "Error in format when adding expense.\n"
            + MESSAGE_USAGE;

    private Expense expense = null;
    private Task task = null;
    private Contact contact = null;
    private Loan loan = null;

    /**
     * Constructs the AddCommand object with Expense object.
     *
     * @param expense The Expense Object.
     */
    public AddCommand(Expense expense) {
        this.expense = expense;
    }

    /**
     * Constructs the AddCommand object with Contract object.
     *
     * @param contact The Contract object.
     */
    public AddCommand(Contact contact) {
        this.contact = contact;
    }

    /**
     * Constructs the AddCommand object with Loan object.
     *
     * @param loan The Loan object.
     */
    public AddCommand(Loan loan) {
        this.loan = loan;
    }

    /**
     * Add the Record objects into their respective lists and returns false.
     *
     * @param wallet The Wallet object.
     * @return a boolean variable which indicates
     */
    @Override
    public boolean execute(Wallet wallet) {
        if (expense != null) {
            wallet.getExpenseList().addExpense(expense);
            wallet.getRecordList().addRecord(expense);
            wallet.getExpenseList().setModified(true);
            System.out.println(MESSAGE_SUCCESS_ADD_EXPENSE);
            System.out.println(expense.toString());
        }
        if (contact != null) {
            wallet.getContactList().addContact(contact);
            wallet.getContactList().setModified(true);
            System.out.println(MESSAGE_SUCCESS_ADD_CONTACT);
            System.out.println(contact.toString());
        }
        if (loan != null) {
            wallet.getLoanList().addLoan(loan);
            wallet.getRecordList().addRecord(loan);
            wallet.getLoanList().setModified(true);
            System.out.println(MESSAGE_SUCCESS_ADD_LOAN);
            Ui.printLoanTableHeaders();
            Ui.printLoanRow(loan);
            Ui.printLoanTableClose();
        }

        return false;
    }
}
