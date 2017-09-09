uniform mat4 u_Matrix;                      // Combined model-view-projection matrix

attribute vec2 a_Position;                  // Vertex position attribute
attribute float a_Amplitude;                // The amplitude for this bar. Should be the same for all vertices of the bar.
attribute float a_IsSkipSilence;            // This represents a bar with skip silence. Should be the same for all vertices of the bar.

varying lowp float v_dBFS;                  // Output dBFS
varying lowp float v_IsSkipSilence;         // Output is skip silence.

float log10(in float operand)
{
    return log(operand) / log(10.0);
}

float amplitudeToDbfs(in float amplitude)
{
    return log10(amplitude) * 20.0;
}

void main()
{
    v_dBFS = amplitudeToDbfs(a_Amplitude);
    v_IsSkipSilence = a_IsSkipSilence;
    gl_Position = u_Matrix * vec4(a_Position, 0, 1);
}