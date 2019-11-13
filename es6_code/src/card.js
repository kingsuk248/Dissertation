class Card {
    constructor(title, text) {
        this.title = title;
        this.text = text;
        this.template = `
            <div class="card" name="${this.text}">
                <div class="card-body">
                    <h1 class="card-title">${this.title}</h2>
                    <h5 class="card-text">${this.text}</h5>
                </div>
            </div>
        `;
    }
    inject(parent) {
        if (parent) {
            parent.insertAdjacentHTML('beforeend', this.template);
        }
    }
}
export default Card;