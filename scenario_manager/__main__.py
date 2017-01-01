#!/usr/bin/env python3

if __name__ == "__main__":
    import sys
    from PyQt5 import QtWidgets
    from scenario_manager import ScenarioManagerMainWindow

    app = QtWidgets.QApplication(sys.argv)
    window = ScenarioManagerMainWindow()
    window.show()
    sys.exit(app.exec_())
