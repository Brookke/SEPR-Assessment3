#!/usr/bin/env python3

import os, os.path, shutil
from jinja2 import Environment, FileSystemLoader

JINJA_EXTENSION = ".jinja"

OUTPUT_FILES = [
    "index.xhtml",
    "designdocs/index.xhtml",
    "plan/index.xhtml",
]

#ROOT_DIR = "https://teamfarce.github.io/MIRCH/"
ROOT_DIR = "/docs/"


TEMPLATE_DIR = "./templates/"
OUTPUT_DIR = "../docs/"
STATIC_DIR = "./static/"

if __name__ == "__main__":
    # Change the current directory to the directory this script resides in
    os.chdir(os.path.dirname(os.path.abspath(__file__)))

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

    # Copy across the static file by scanning the static directory.
    for current_walk_directory, child_directories, child_files in os.walk(STATIC_DIR):
        # Strip the "./static/" prefix if it exists for the target file.
        if current_walk_directory.startswith(STATIC_DIR):
            root_relative_walk_directory = current_walk_directory[len(STATIC_DIR):]
        else:
            root_relative_walk_directory = current_walk_directory

        # For all of the child directories, create them in the output directory.
        for child_directory in child_directories:
            new_dir = os.path.join(OUTPUT_DIR, root_relative_walk_directory, child_directory)
            try:
                os.makedirs(new_dir, exist_ok = True)
            except:
                pass

        # For all of the child files, copy them into the output directory.
        for child_file in child_files:
            source_file = os.path.join(current_walk_directory, child_file)
            target_file = os.path.join(OUTPUT_DIR, root_relative_walk_directory, child_file)
            shutil.copyfile(source_file, target_file)
