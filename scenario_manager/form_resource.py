#!/usr/bin/env python3

import os.path
import PyQt5.uic
from PyQt5.QtCore import Qt
from PyQt5 import QtCore, QtWidgets, QtGui, QtSql

source_location = os.path.join(os.path.dirname(__file__), "ui_forms/")
resource_form_source = os.path.join(source_location, "form_resource.ui")
UiResourceFrom = PyQt5.uic.loadUiType(resource_form_source)[0]

class ResourceForm(UiResourceFrom, QtWidgets.QWidget):
    """This class represents the form for manipulating the resources."""

    def __init__(self, parent = None):
        super().__init__(parent)
        self.setupUi(self)

        self._database = None
        self._resource_root = "./"
        self._model = None

    def set_database(self, database):
        """Set a new database to interact with."""

        self._database = database
        self._reset_model()

    def set_resource_root(self, resource_root):
        """Set a new resource root to use internally."""

        self._resource_root = resource_root
        self._reset_model()

    def _reset_model(self):
        # Adapts the form to a change of model or resource root.
        if self._database:
            self._model = ResourceModel(self, self._database, self._resource_root)
            self.resource_list.setModel(self._model)
            self.resource_list.setModelColumn(1)
        else:
            self.resource_list.setModel(None)

    @QtCore.pyqtSlot()
    def add_resource(self):
        if self._model:
            new_index = self._model.rowCount()
            self._model.insertRows(new_index, 1)
            self.resource_list.edit(self._model.index(new_index, 1))

    @QtCore.pyqtSlot()
    def remove_resources(self):
        if self._model:
            for index in self.resource_list.selectedIndexes():
                self._model.removeRow(index.row(), index.parent())
            self._model.select()

class ResourceModel(QtSql.QSqlTableModel):
    """This class represents the database table resources."""

    def __init__(self, parent, database, resource_root = None):
        super().__init__(parent, database)
        self.setTable("resources")
        self.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.select()
        self._resource_root = resource_root

    def data(self, index, role = Qt.DisplayRole):
        # This method is overridden in order to provide icons of the resources themselves. This is
        # done by providing a valid icon for the role "EditRole".

        if index.column() == 1 and role == Qt.DecorationRole:
            # The filename of the icon we are to load should be the same data in the database
            # itself. We can extract this information from the model by using the very function
            # we are in; just by modifying the intended role but maintaining the index.
            stored_filename = self.data(index, Qt.EditRole)
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
            if self._resource_root:
                icon_filenames.insert(0, os.path.join(self._resource_root, stored_filename))

            # Check the validity of the filenames be checking to see if the file exits.
            valid_icon_filenames = list(filter(os.path.exists, icon_filenames))
            if len(valid_icon_filenames) == 0:
                return None
            icon_filename = valid_icon_filenames[0]
            return QtGui.QIcon(icon_filename)
        else:
            # Don't touch the other columns/roles. Delegate this to the parent.
            return super().data(index, role)
