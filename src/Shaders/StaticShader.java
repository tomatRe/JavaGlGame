package Shaders;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/Shaders/VertexShader";
    private static final String FRAGMENT_FILE = "src/Shaders/FragmentShader";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void BindAttributes() {
        super.BindAttribute(0,"position");
    }
}
