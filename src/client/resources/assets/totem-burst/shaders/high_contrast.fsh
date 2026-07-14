#version 150

uniform sampler2D InSampler;
uniform IntensityConfig {
    float Intensity;
};

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 original = texture(InSampler, texCoord);

    float contrastAmount = 1.0 + Intensity * 3.0;
    vec3 contrasted = (original.rgb - 0.5) * contrastAmount + 0.5;
    contrasted = clamp(contrasted, 0.0, 1.0);

    float luma = dot(contrasted, vec3(0.299, 0.587, 0.114));
    vec3 saturated = mix(vec3(luma), contrasted, 1.0 + Intensity * 0.3);

    fragColor = mix(original, vec4(clamp(saturated, 0.0, 1.0), original.a), Intensity);
}