#version 150

uniform sampler2D InSampler;
uniform IntensityConfig {
    float Intensity;
};

in vec2 texCoord;
out vec4 fragColor;

const int SAMPLES = 12;

float ScreenSpaceNoise(vec2 uv) {
    return fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43758.5453123);
}

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 dir = center - texCoord;

    float blurRadius = Intensity * 0.05;

    float noise = ScreenSpaceNoise(texCoord);

    vec4 sum = vec4(0.0);
    float totalWeight = 0.0;

    for (int i = 0; i < SAMPLES; i++) {
        float t = (float(i) + noise) / float(SAMPLES);

        vec2 offset = dir * blurRadius * t;
        vec4 s = texture(InSampler, texCoord + offset);

        float weight = 1.0;

        sum += s * weight;
        totalWeight += weight;
    }

    vec4 blurred = sum / max(totalWeight, 0.001);

    fragColor = clamp(blurred, 0.0, 1.0);
}
