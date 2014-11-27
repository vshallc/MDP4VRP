package main;

import mdp.MDP;
import vrp.VRP;

import java.io.*;

/**
 * Created by Xiaoxi Wang on 11/17/14.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("cfg vrp mdp pic deg sim");
            System.exit(2);
        }
        String cfg_path = args[0];
        String vrp_path = args[1];
        String mdp_path = args[2];
        String pic_path = args[3]; // save statics of pieces number
        String deg_path = args[4]; // save statics of degree number
        String sim_path = args[5]; // save simulation results

        int vrp_type;
        int vrp_size_r, vrp_size_c;
        int vrp_task_num;

        VRP vrp;
        MDP mdp;
        double[] linear_approx = {0.20, 0.3, 0.5, 1, 2};

        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(cfg_path));
            String[] configs = br.readLine().split(" ");
            vrp_type = Integer.valueOf(configs[0]);
            vrp_size_r = Integer.valueOf(configs[1]);
            vrp_size_c = Integer.valueOf(configs[2]);
            vrp_task_num = Integer.valueOf(configs[3]);
            br.close();

//            if (vrp_type == 1) {
            vrp = VRP.VRPGenerator_MeshMap(vrp_size_r, vrp_size_c, vrp_task_num);
//            }




            File vrp_file = new File(vrp_path);
            if (!vrp_file.exists()) vrp_file.createNewFile();
            BufferedWriter bw_vrp = new BufferedWriter(new FileWriter(vrp_file.getAbsoluteFile()));
            bw_vrp.write(vrp.toString());
            bw_vrp.close();



            File mdp_file = new File(mdp_path);
            if (!mdp_file.exists()) mdp_file.createNewFile();
            BufferedWriter bw_mdp = new BufferedWriter(new FileWriter(mdp_file.getAbsoluteFile()));

            File pic_file = new File(pic_path);
            if (!pic_file.exists()) pic_file.createNewFile();
            BufferedWriter bw_pic = new BufferedWriter(new FileWriter(pic_file.getAbsoluteFile()));

            File deg_file = new File(deg_path);
            if (!deg_file.exists()) deg_file.createNewFile();
            BufferedWriter bw_deg = new BufferedWriter(new FileWriter(deg_file.getAbsoluteFile()));

            File sim_file = new File(sim_path);
            if (!sim_file.exists()) sim_file.createNewFile();
            BufferedWriter bw_sim = new BufferedWriter(new FileWriter(sim_file.getAbsoluteFile()));


            for (int i = 0; i < linear_approx.length; ++i) {
                mdp = new MDP(vrp, linear_approx[i]);
                mdp.buildGraph();
                System.out.println("Graph done");
                mdp.assignValueFunction();
                System.out.println("Value done");

//                bw_mdp.write("MDP Graph:\n");
//                bw_mdp.write(mdp.valueFunctionToString());
//                bw_mdp.write("\n");
//                bw_mdp.write("MDP Policies:\n");
//                bw_mdp.write(mdp.policyToString());
//                bw_mdp.write("\n");

                bw_pic.write(String.valueOf(mdp.getAvgPieceNum()));
                bw_pic.write("\t");

                bw_deg.write(String.valueOf(mdp.getAvgDegreeNum()));
                bw_deg.write("\t");

                double sim = 0;
                for (int j = 0; j < 1000; ++j) {
                    sim += mdp.simulate();
                }
                sim /= 1000.0;
                bw_sim.write(String.valueOf(sim));
                bw_sim.write("\t");
            }
            bw_pic.write("\n");
            bw_deg.write("\n");
            bw_sim.write("\n");
            bw_mdp.close();
            bw_pic.close();
            bw_deg.close();
            bw_sim.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
