class Header {
    constructor() {
        this.template = `
            <div class="profile_container">
            <a href="#"><img src="https://icon-library.net//images/profile-image-icon/profile-image-icon-5.jpg" width="48px" /></a>
            </div>
        `
    }
    inject(parent) {
        if (parent) {
            parent.insertAdjacentHTML('beforeend', this.template);
        }
    }
}
export default Header;