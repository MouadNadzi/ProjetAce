import UIKit

// Model
class ${projectName}Model {
    private var data: String

    init(data: String) {
        self.data = data
    }

    func getData() -> String {
        return data
    }

    func setData(_ data: String) {
        self.data = data
    }
}

// Controller
class ${projectName}Controller {
    private var model: ${projectName}Model
    private weak var view: ${projectName}ViewController?

    init(view: ${projectName}ViewController) {
        self.view = view
        self.model = ${projectName}Model(data: "")
    }

    func updateData(_ data: String) {
        model.setData(data)
        view?.displayData(data)
    }
}

// View
class ${projectName}ViewController: UIViewController {
    private var controller: ${projectName}Controller!

    override func viewDidLoad() {
        super.viewDidLoad()
        controller = ${projectName}Controller(view: self)
    }

    func displayData(_ data: String) {
        // Update UI with data
    }
}
