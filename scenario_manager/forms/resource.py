#!/usr/bin/env python3

import os.path
import PyQt5.uic
from PyQt5.QtCore import Qt
from PyQt5 import QtCore, QtWidgets, QtGui, QtSql
from common import resource_icon, UI_SOURCE_LOCATION, DatabaseResourceForm

RESOURCE_FORM_SOURCE = os.path.join(UI_SOURCE_LOCATION, "form_resource.ui")
UiResourceFrom = PyQt5.uic.loadUiType(RESOURCE_FORM_SOURCE)[0]

class ResourceForm(UiResourceFrom, DatabaseResourceForm):
    """This class represents the form for manipulating the resources."""

    def __init__(self, parent = None):
        super().__init__(parent)
        self.setupUi(self)

    def reset_model(self):
        # Implementation of the parent's abstract method.
        if self.database:
            self.model = ResourceModel(self, self.database, self.resource_root)
            self.resource_list.setModel(self.model)
            self.resource_list.setModelColumn(1)
        else:
            self.resource_list.setModel(None)

    @QtCore.pyqtSlot()
    def add_resource(self):
        """Adds a resource to the end of the list."""

        if self.model:
            new_index = self.model.rowCount()
            self.model.insertRows(new_index, 1)
            self.resource_list.edit(self.model.index(new_index, 1))

    @QtCore.pyqtSlot()
    def remove_resources(self):
        """Removes resources based on the indices selected in the view."""

        if self.model:
            for index in self.resource_list.selectedIndexes():
                self.model.removeRow(index.row(), index.parent())
            self.model.select()

class ResourceModel(QtSql.QSqlTableModel):
    """This class represents the database table resources."""

    def __init__(self, parent, database, resource_root = None):
        super().__init__(parent, database)
        self.setTable("resources")
        self.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.select()
        self.resource_root = resource_root

    def data(self, index, role = Qt.DisplayRole):
        # This method is overridden in order to provide icons of the resources themselves. This is
        # done by providing a valid icon for the role "DecorationRole".

        if index.column() == 1 and role == Qt.DecorationRole:
            return resource_icon(self, index, self.resource_root)
        else:
            # Don't touch the other columns/roles. Delegate this to the parent.
            return super().data(index, role)
