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
        MDP mdp0, mdp1, mdp2;

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
            bw_mdp.write("mdp");
            bw_mdp.close();

            File pic_file = new File(pic_path);
            if (!pic_file.exists()) pic_file.createNewFile();
            BufferedWriter bw_pic = new BufferedWriter(new FileWriter(pic_file.getAbsoluteFile()));
            bw_pic.write("pic");
            bw_pic.close();

            File deg_file = new File(deg_path);
            if (!deg_file.exists()) deg_file.createNewFile();
            BufferedWriter bw_deg = new BufferedWriter(new FileWriter(deg_file.getAbsoluteFile()));
            bw_deg.write("deg");
            bw_deg.close();

            File sim_file = new File(sim_path);
            if (!sim_file.exists()) sim_file.createNewFile();
            BufferedWriter bw_sim = new BufferedWriter(new FileWriter(sim_file.getAbsoluteFile()));
            bw_sim.write("sim");
            bw_sim.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
