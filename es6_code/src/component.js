class Component {
    constructor(_props) {
        this.template = _props.template;
    }
    inject(parent) {
        if (parent && this.template) {
            parent.insertAdjacentHTML('beforeend', this.template);
        }
    }
}