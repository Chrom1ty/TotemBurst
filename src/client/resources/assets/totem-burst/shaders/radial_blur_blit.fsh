#version 150

uniform sampler2D InSampler;
uniform sampler2D OriginalSampler;
uniform IntensityConfig {
    float Intensity;
};

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 blurred = texture(InSampler, texCoord);
    vec4 original = texture(OriginalSampler, texCoord);
    fragColor = mix(original, blurred, Intensity);
}
