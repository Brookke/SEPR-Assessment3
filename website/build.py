#!/usr/bin/env python3

import os, os.path
from jinja2 import Environment, FileSystemLoader

jinja_extension = ".jinja"

output_files = [
    "index.xhtml",
    "designdocs/index.xhtml",
]

root_dir = "https://comprs.github.io/TeamFarce-InitialRepo/"

template_dir = "./templates/"
output_dir = "./output/"

def prepare_output_dir():
    for output_file in output_files:
        destination_file = os.path.join(output_dir, output_file)
        needed_dir = os.path.dirname(destination_file)
        os.makedirs(needed_dir, exist_ok = True)

def write_templates():
    env = Environment(loader = FileSystemLoader(template_dir))
    for output_file in output_files:
        template_location = output_file + jinja_extension
        template = env.get_template(template_location)
        output_string = template.render(root_dir = root_dir)
        with open(os.path.join(output_dir, output_file), "w") as f:
            f.write(output_string)

if __name__ == "__main__":
    prepare_output_dir()
    write_templates()
