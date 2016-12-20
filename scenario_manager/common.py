#!/usr/bin/env python3

import os.path
from PyQt5.QtCore import Qt
from PyQt5 import QtGui

source_location = os.path.join(os.path.dirname(__file__), "ui_forms/")

def resource_icon(model, index, resource_root = None):
    # The filename of the icon we are to load should be the same data in the database
    # itself. We can extract this information from the model by using the very function
    # we are in; just by modifying the intended role but maintaining the index.
    stored_filename = model.data(index, Qt.EditRole)
    if not stored_filename:
        return None

    # We can try three different methods to find the file to load. The first method is to
    # prepend the resource root (the primary use of this variable). The second is to load
    # the file with the filename, as is. The last is to prepend "./". We do not consider
    # the first method if the resource root variable is invalid.
    icon_filenames = [
        stored_filename,
        os.path.join("./", stored_filename),
    ]
    if resource_root:
        icon_filenames.insert(0, os.path.join(resource_root, stored_filename))

    # Check the validity of the filenames be checking to see if the file exits.
    valid_icon_filenames = list(filter(os.path.exists, icon_filenames))
    if len(valid_icon_filenames) == 0:
        return None
    icon_filename = valid_icon_filenames[0]
    return QtGui.QIcon(icon_filename)
