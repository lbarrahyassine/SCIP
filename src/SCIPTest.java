import jscip.*;

import java.io.File;

import static jscip.SCIP_Vartype.SCIP_VARTYPE_CONTINUOUS;



public class SCIPTest {
    public static void main(String[] args) {

        System.load(new File("lib/libscip.dll").getAbsolutePath());
        System.load(new File("lib/libjscip.dll").getAbsolutePath());
        System.out.println("Java Library Path: " + System.getProperty("java.library.path"));

        /*try {
            System.load("the absolute path to libscip.dll");
            System.out.println("libscip.dll loaded successfully.");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Error loading libscip.dll: " + e.getMessage());
        }
        try {
            System.load("the absolute path yo libjscip.dll");
            System.out.println("libjscip.dll loaded successfully.");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Error loading libjscip.dll: " + e.getMessage());
        }
        */


        // Create a SCIP instance
        Scip scip = new Scip();
        scip.create("TestProblem"); // Initialize the SCIP problem

        // Create variables
        Variable x = scip.createVar("x", 0.0, Double.POSITIVE_INFINITY, 3.0, SCIP_VARTYPE_CONTINUOUS);
        Variable y = scip.createVar("y", 0.0, Double.POSITIVE_INFINITY, 4.0, SCIP_VARTYPE_CONTINUOUS);

        // Create a linear constraint: x + 2y <= 14
        Variable[] vars = {x, y};
        double[] coeffs = {1.0, 2.0};
        Constraint cons = scip.createConsLinear("cons1", vars, coeffs, -Double.POSITIVE_INFINITY, 3.0);

        // Add the constraint to the model
        scip.addCons(cons);

        // Set the objective sense to maximization
        scip.setMaximize();

        // Solve the problem
        scip.solve();

        // Print the solution
        if (scip.getStatus() == SCIP_Status.SCIP_STATUS_OPTIMAL) {
            System.out.println("Optimal value: " + scip.getPrimalbound());
            System.out.println("x = " + scip.getSolVal(scip.getBestSol(), x));
            System.out.println("y = " + scip.getSolVal(scip.getBestSol(), y));
        } else {
            System.out.println("No optimal solution found.");
        }

        // Release the constraint
        scip.releaseCons(cons);

        // Release the SCIP instance
        scip.free();
    }
}