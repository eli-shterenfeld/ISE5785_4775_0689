import java.io.IOException;  // Import the IOException class (although not used in the current code)

/**
 * Main class to calculate and display payroll information.
 */
public class Payroll {
    /**
     * The main method to execute the payroll system.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Employee[] emp = new Employee[3];  // Create an array to hold Employee objects (of different types)

        emp[0] = new HourlyEmployee("ali", "shteren", 323054775, 30, 50);  // Initialize an HourlyEmployee object
        emp[1] = new CommissionEmployee("david", "gindy", 331672204, 3, 34589.63f);  // Initialize a CommissionEmployee object
        emp[2] = new BasePlusCommissionEmployee("tomas", "shelvi", 307212431, 4, 45696.79f, 59);  // Initialize a BasePlusCommissionEmployee object

        for (Employee Emp : emp) {  // Loop through each employee in the array
            /*
             * If the employee is an instance of BasePlusCommissionEmployee,
             * increase their base salary by 10%.
             */
            if (Emp instanceof BasePlusCommissionEmployee) {
                BasePlusCommissionEmployee bese = (BasePlusCommissionEmployee) Emp;
                bese.SetBaseSalary(bese.GetBaseSalary() * 1.10f);
            }

            // Print the employee's details using toString()
            System.out.println(Emp);
            System.out.println("his earn: " + Emp.earning());
        }
    }
}
