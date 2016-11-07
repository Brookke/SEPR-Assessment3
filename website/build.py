#!/usr/bin/env python3

import os, os.path, subprocess
from glob import glob
from jinja2 import Environment, FileSystemLoader

JINJA_EXTENSION = ".jinja"

OUTPUT_FILES = [
    "index.xhtml",
    "designdocs/index.xhtml",
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
    render_jinja_template = lambda x: env.get_template(x).render(root_dir = ROOT_DIR)

    # A function to convert the relative output file into a path for use by the template renderer.
    template_jinja_path = lambda x: x + JINJA_EXTENSION

    # A function which takes an output file's path and produces the rendered output which should
    # be placed into the file.
    output_file_jinja_render = lambda x: render_jinja_template(template_jinja_path(x))

    # Render all of the templates. Evaluate the iterator to make sure any render errors occur here
    # rather than when we begin to write out.
    rendered = list(map(output_file_jinja_render, OUTPUT_FILES))

    # Get and iterate though the directories that need to be created and create them. The
    # directories that need created are the output files with the output directory prepended and
    # the filename removed.
    for needed_dir in map(lambda x: os.path.dirname(os.path.join(OUTPUT_DIR, x)), OUTPUT_FILES):
        os.makedirs(needed_dir, exist_ok = True)

    # Iterate though the rendered output and zip with the file to output the result to. Combine
    # these to write out to file.
    for rendered, output_file in zip(rendered, OUTPUT_FILES):
        with open(os.path.join(OUTPUT_DIR, output_file), "w") as f:
            f.write(rendered)

    # Copy the static directory into the output directory. It uses a external program since this
    # easier to do than in python.
    subprocess.run(["cp", "-r"] + glob(STATIC_DIR + "*") + [OUTPUT_DIR])
