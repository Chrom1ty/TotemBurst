#version 150

uniform sampler2D InSampler;
uniform IntensityConfig {
    float Intensity;
};

in vec2 texCoord;
out vec4 fragColor;

const int SAMPLES = 12;

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 dir = center - texCoord;

    float blurAmount = Intensity * 0.07;

    vec4 sum = vec4(0.0);
    float totalWeight = 0.0;

    for (int i = 0; i < SAMPLES; i++) {
        float t = float(i) / float(SAMPLES - 1);
        vec2 offset = dir * blurAmount * t;
        vec4 s = texture(InSampler, texCoord + offset);

        float weight = 1.0 - t * 0.4;
        sum += s * weight;
        totalWeight += weight;
    }

    vec4 blurred = sum / totalWeight;
    vec4 original = texture(InSampler, texCoord);

    fragColor = mix(original, blurred, Intensity);
}
