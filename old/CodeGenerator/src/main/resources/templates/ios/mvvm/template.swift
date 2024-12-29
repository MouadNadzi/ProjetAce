import UIKit
import Combine

// Model
struct ${projectName}Model {
    var data: String
}

// ViewModel
class ${projectName}ViewModel {
    @Published private(set) var model: ${projectName}Model

    init() {
        self.model = ${projectName}Model(data: "")
    }

    func updateData(_ data: String) {
        model = ${projectName}Model(data: data)
    }
}

// View
class ${projectName}ViewController: UIViewController {
    private var viewModel: ${projectName}ViewModel!
    private var cancellables = Set<AnyCancellable>()

    override func viewDidLoad() {
        super.viewDidLoad()

        viewModel = ${projectName}ViewModel()
        setupBindings()
    }

    private func setupBindings() {
        viewModel.$model
            .sink { [weak self] model in
                // Update UI with model.data
            }
            .store(in: &cancellables)
    }
}