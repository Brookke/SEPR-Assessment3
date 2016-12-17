#!/usr/bin/env python3

import os, os.path, subprocess
from glob import glob
from jinja2 import Environment, FileSystemLoader

JINJA_EXTENSION = ".jinja"

OUTPUT_FILES = [
    "index.xhtml",
    "designdocs/index.xhtml",
    "plan/index.xhtml",
]

ROOT_DIR = "https://teamfarce.github.io/MIRCH/"

TEMPLATE_DIR = "./templates/"
OUTPUT_DIR = "../docs/"
STATIC_DIR = "./static/"

if __name__ == "__main__":
    # Change the current directory to the directory this script resides in
    os.chdir(os.path.dirname(__file__))

    # Get a jinja environment to load templates from the template directory.
    env = Environment(loader = FileSystemLoader(TEMPLATE_DIR))

    # A function which renders a template taking a template path. It uses the jinja environment
    # defined earlier and the root directory constant.
    def render(template_file):
        return env.get_template(template_file).render(root_dir = ROOT_DIR)

    # Create an iterable for all of the input templates by appending the template file extension
    # onto the expected output files. The jinja environment resolves relative to the template root
    # directory that doesn't need to be appended.
    input_templates = (output_file + JINJA_EXTENSION for output_file in OUTPUT_FILES)

    # Render all of the templates. Any errors the templates may cause are raise here and shouldn't
    # cause errors whilst we are trying to write to the filesystem.
    rendered_output = list(map(render, input_templates))

    # Start writing out the rendered files. To this by iterating over the rendered results along
    # with the final filenames.
    for rendered_buffer, output_filename in zip(rendered_output, OUTPUT_FILES):
        # Attach the root directory to write into to the filename.
        full_output_filename = os.path.join(OUTPUT_DIR, output_filename)
        os.makedirs(os.path.dirname(full_output_filename), exist_ok = True)
        with open(full_output_filename, "wt") as f:
            f.write(rendered_buffer)

    # Copy the static directory into the output directory. It uses a external program since this
    # easier to do than in python.
    subprocess.run(["cp", "-r"] + glob(STATIC_DIR + "*") + [OUTPUT_DIR])
