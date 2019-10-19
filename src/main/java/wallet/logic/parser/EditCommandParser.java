package wallet.logic.parser;

import wallet.logic.LogicManager;
import wallet.logic.command.EditCommand;
import wallet.model.contact.Contact;
import wallet.model.record.Expense;
import wallet.model.record.Loan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The EditCommandParser class helps to
 * change user input String into appropriate parameters.
 */
public class EditCommandParser implements Parser<EditCommand> {

    @Override
    public EditCommand parse(String input) {
        String[] arguments = input.split(" ", 2);
        switch (arguments[0]) {
        case "expense":
            Expense expense = parseExpense(arguments[1]);
            if (expense != null) {
                LogicManager.getCommandHistory().add("edit " + arguments[0] + " " + arguments[1]);
                return new EditCommand(expense);
            } else {
                break;
            }

        case "loan":
            Loan loan = parseLoan(arguments[1]);
            if(loan != null) {
                LogicManager.getCommandHistory().add("edit " + arguments[0] + " " + arguments[1]);
                return new EditCommand(loan);
            }
            break;

        case "contact":
            Contact contact = parseContact(arguments[1]);
            if (contact != null) {
                LogicManager.getCommandHistory().add("edit " + arguments[0] + " " + arguments[1]);
                return new EditCommand(contact);
            } else {
                break;
            }

        default:
            System.out.println(EditCommand.MESSAGE_USAGE);
            return null;
        }
        return null;
    }

    /**
     * Parses the parameters of contact to be edited.
     *
     * @param input User input arguments
     */
    private Contact parseContact(String input) throws NumberFormatException, ArrayIndexOutOfBoundsException {

        String[] arguments = input.split(" ", 2);
        if (arguments.length == 2) {

            String[] parameters = arguments[1].split(" ");
            try {
                int id = Integer.parseInt(arguments[0].trim());
                ContactParserHelper contactHelper = new ContactParserHelper();
                Contact contact = contactHelper.updateInput(parameters);
                contact.setId(id);
                return contact;
            } catch (NumberFormatException e) {
                return null;
            }

        }
        return null;

    }

    /**
     * Parses the parameters of contact to be edited.
     *
     * @param input User input arguments
     */
    private Loan parseLoan(String input) throws NumberFormatException, ArrayIndexOutOfBoundsException {

        Loan loan = new Loan();

        String[] arguments = input.split(" ", 2);
        int loan_id = Integer.parseInt(arguments[0].trim());
        loan.setId(loan_id);
        String parameters = arguments[1].trim();

        if (parameters.contains("/c")) {
            String[] getContact = parameters.split("/c");
            int contact_id = Integer.parseInt(getContact[1].trim());
            for(Contact contact : LogicManager.getWallet().getContactList().getContactList()) {
                if(contact.getId() == contact_id) {
                    System.out.println("Edit: Contact found! " + contact.toString());
                    loan.setPerson(contact);
                    break;
                }
            }
            parameters = getContact[0].trim();
        }
        if (parameters.contains("/l")) {
            String[] getIsLend = parameters.split("/l");
            System.out.println("Edit: lend found!");
            loan.setIsLend(true);
            parameters = getIsLend[0].trim();
        } else if (parameters.contains("/b")) {
            String[] getIsLend = parameters.split("/b");
            System.out.println("Edit: borrow found!");
            loan.setIsLend(false);
            parameters = getIsLend[0].trim();
        } else if (!parameters.contains("/l") || !parameters.contains("/b")) {
            int index = LogicManager.getWallet().getLoanList().findIndexWithId(loan.getId());
            Loan currentLoan = LogicManager.getWallet().getLoanList().getLoan(index);
            System.out.println("Edit: borrow/loan not found! "
                    + "currentLoan.getIsLend(): " + currentLoan.getIsLend());
            loan.setIsLend(currentLoan.getIsLend());
        }
        if (parameters.contains("/t")) {
            String[] getDate = parameters.split("/t");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate createdDate = LocalDate.parse(getDate[1].trim(), formatter);
            System.out.println("Edit: date found!");
            loan.setCreatedDate(createdDate);
            System.out.println("EditCommandParser loan.getCreatedDate(): "+ loan.getCreatedDate());
            parameters = getDate[0].trim();
        } else if (!parameters.contains("/t")) {
            int index = LogicManager.getWallet().getLoanList().findIndexWithId(loan.getId());
            Loan currentLoan = LogicManager.getWallet().getLoanList().getLoan(index);
            loan.setCreatedDate(currentLoan.getCreatedDate());
            System.out.println("EditCommandParser loan.getCreatedDate(): "+ loan.getCreatedDate());
        }
        if (parameters.contains("/a")) {
            String[] getAmount = parameters.split("/a");
            double amount = Double.parseDouble(getAmount[1].trim());
            System.out.println("Edit: amount found!");
            loan.setAmount(amount);
            System.out.println("EditCommandParser loan.getAmount(): "+ loan.getAmount());
        }
        if (parameters.contains("/d")) {
            String[] getDescription = parameters.split("/d");
            String description = getDescription[1].trim();
            System.out.println("Edit: description found!");
            loan.setDescription(description);
        }
        return loan;
    }

    /**
     * Parses the parameters of expense to be edited.
     */
    public Expense parseExpense(String input) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        Expense expense = new Expense();

        String[] arguments = input.split(" ", 2);
        int id = Integer.parseInt(arguments[0].trim());
        expense.setId(id);
        String parameters = arguments[1].trim();
        if (parameters.contains("/r")) {
            String[] getRecurring = parameters.split("/r");
            if (getRecurring[1].trim().equalsIgnoreCase("DAILY")
                    || getRecurring[1].trim().equalsIgnoreCase("WEEKLY")
                    || getRecurring[1].trim().equalsIgnoreCase("MONTHLY")) {
                expense.setRecurring(true);
                expense.setRecFrequency(getRecurring[1].trim().toUpperCase());
            } else if (getRecurring[1].trim().equals("no")) {
                expense.setRecurring(false);
                expense.setRecFrequency(null);
            }
            parameters = getRecurring[0].trim();
        }

        if (parameters.contains("/c")) {
            String[] getCategory = parameters.split("/c");
            expense.setCategory(getCategory[1].trim());
            parameters = getCategory[0].trim();
        }

        if (parameters.contains("/a")) {
            String[] getAmount = parameters.split("/a");
            expense.setAmount(Double.parseDouble(getAmount[1].trim()));
            parameters = getAmount[0].trim();
        }

        if (parameters.contains("/d")) {
            String[] getDescription = parameters.split("/d");
            expense.setDescription(getDescription[1].trim());
        }

        return expense;
    }
}
