#!/usr/bin/env python3

import os.path
import PyQt5.uic
from PyQt5.QtCore import Qt
from PyQt5 import QtCore, QtWidgets, QtGui, QtSql
from common import resource_icon, source_location

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
        # done by providing a valid icon for the role "DecorationRole".

        if index.column() == 1 and role == Qt.DecorationRole:
            return resource_icon(self, index, self._resource_root)
        else:
            # Don't touch the other columns/roles. Delegate this to the parent.
            return super().data(index, role)
