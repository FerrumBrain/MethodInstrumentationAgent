import java.lang.instrument.Instrumentation;

public class MethodInstrumentationAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new TimingClassFileTransformer());
    }
}