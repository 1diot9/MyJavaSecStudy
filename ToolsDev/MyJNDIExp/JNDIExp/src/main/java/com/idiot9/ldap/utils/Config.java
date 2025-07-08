package com.idiot9.ldap.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.UnixStyleUsageFormatter;
import com.idiot9.ldap.enumtypes.EncodingTypes;
import com.idiot9.ldap.gadgets.GadgetFactory;
import org.reflections.Reflections;

import java.util.*;

//参数解析
public class Config {
    @Parameter(names = {"-u", "--usage"}, description = "show available gadgets")
    public static boolean usage;

    @Parameter(names = {"-g", "--gadget"}, description = "choose gadget name", order = 2 ,required = true)
    public static String gadget;

    @Parameter(names = {"-c", "--cmd"}, description = "cmd you want to execute", order = 2)
    public static String cmd = "calc";

    @Parameter(names = {"-e", "--encode"}, description = "choose encode pattern | support: bin,base64,base64url,hex", order = 2)
    public static String encode = "bin";

    @Parameter(names = {"-m", "--memshell"}, description = "generate memshell", order = 3)
    public static String mem;

    @Parameter(names = {"-p", "--password"}, description = "password of memshell like ?cmd=", order = 3)
    public static String password = "cmd";

    @Parameter(names = {"-h", "--help"}, description = "show helps", help = true)
    public static boolean help = false;

    public Config() {
    }

    public static void applyArgs(String[] agrs){
        JCommander jc = JCommander.newBuilder().addObject(new Config()).build();

        try{
            jc.parse(agrs);
        }catch(Exception e){
            System.out.print("");
            help = true;
        }

        if (usage){
            Reflections.log = null;
            final List<Class<? extends GadgetFactory>> payloadClasses =
                    new ArrayList<Class<? extends GadgetFactory>>(GadgetFactory.Utils.getPayloadClasses());
            //按字母排序
            Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize

            final List<String[]> rows = new LinkedList<String[]>();

            for (Class<? extends GadgetFactory> payloadClass : payloadClasses) {
                rows.add(new String[] {
                        payloadClass.getSimpleName(),
                });
            }

            //解析表格，就是对String[]进行排版
            final List<String> lines = Strings.formatTable(rows);
            System.out.println("Supported Gadgets:");
            for (String line : lines) {
                System.err.println("     " + line);
            }

        }

        jc.setProgramName("java -jar test.jar");
        jc.setUsageFormatter(new UnixStyleUsageFormatter(jc));

        if(help) {
            jc.usage(); //if -h specified, show help and exit
            System.exit(0);
        }

    }
}
