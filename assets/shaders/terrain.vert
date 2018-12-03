#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 tc;

uniform mat4 pr_matrix;
uniform mat4 mv_matrix;

out DATA
{
    vec2 tc;
} vs_out;

void main()
{
    vec4 new_position = mv_matrix * position;
    gl_Position = pr_matrix * new_position;
    vs_out.tc = tc;
}