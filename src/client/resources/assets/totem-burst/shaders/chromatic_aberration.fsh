#version 150

uniform sampler2D InSampler;
uniform IntensityConfig {
    float Intensity;
};

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 dir = texCoord - center;

    vec2 offsetR = texCoord + dir * Intensity * 0.05;
    vec2 offsetG = texCoord;
    vec2 offsetB = texCoord - dir * Intensity * 0.05;

    float r = texture(InSampler, offsetR).r;
    float g = texture(InSampler, offsetG).g;
    float b = texture(InSampler, offsetB).b;
    float a = texture(InSampler, texCoord).a;

    vec4 aberrated = vec4(r, g, b, a);
    vec4 original = texture(InSampler, texCoord);

    fragColor = mix(original, aberrated, Intensity);
}