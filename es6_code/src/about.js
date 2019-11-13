class About {
    constructor() {
        this.template = `
            <div class="row" id="about">
                <h3 class="col-xs-12">About</h3>
                <h6 class="col-xs-12"></h6>
            </div>
        `;
    }
    inject(parent) {
        if (parent) {
            parent.insertAdjacentHTML('beforeend', this.template);
        }
    }
}
export default About;