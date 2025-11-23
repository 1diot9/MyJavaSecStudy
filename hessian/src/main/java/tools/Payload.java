package tools;

public abstract class Payload {
//    public Templates getTemplates(byte[] byteCode, String bytePath) throws Exception {
//        Templates templates = TemplatesGen.getTemplates(byteCode, bytePath);
//        return templates;
//    }

    public abstract String getPayload(byte[] byteCode, String bytePath) throws Exception;
}
