precision mediump float;

uniform lowp vec3 u_BarColor;  			    // The bar color
uniform lowp vec3 u_NearMaxBarColor;		// The near-max bar color
uniform lowp vec4 u_SkipSilenceBarColor;	// The skipping silence bar color
uniform lowp float u_Alpha;                 // The alpha value to apply to everything.

varying lowp float v_dBFS;                  // The dBFS for this bar. Should be the same for all fragments of the bar.
varying lowp float v_IsSkipSilence;         // The skip silence state for this bar. Should be the same for all fragments of the bar.

void main()
{
    if (v_dBFS > -1.0) {
        gl_FragColor = vec4(u_NearMaxBarColor, u_Alpha);
    } else {
        if (v_IsSkipSilence > 0.0) {
            gl_FragColor = vec4(u_SkipSilenceBarColor.rgb, u_SkipSilenceBarColor.a * u_Alpha);
        } else {
            gl_FragColor = vec4(u_BarColor, u_Alpha);
        }
    }
}