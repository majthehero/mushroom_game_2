#version 330 core

in DATA
{
    vec2 tc;
} fs_in;

uniform sampler2D tex;

void main()
{
    gl_FragColor = texture(tex, fs_in.tc);
}