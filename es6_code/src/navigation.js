class Navigation {
    constructor() {
        this.template = `
        <div class="sidebar-sticky">
            <div class="nav-header">
                <h3 class="nav-item">Dissertation</h3>
            </div>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link item-db active" href="#"><i class="fa fa-bar-chart" aria-hidden="true"></i><span class="hidden-sm-down">Dashboard</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link item-about" href="#about"><i class="fa fa-info-circle" aria-hidden="true"></i><span class="hidden-sm-down">About</span></a>
                </li>
            </ul>
        </div>
        `;
    }
    inject(parent, w, d) {
        parent.insertAdjacentHTML('beforeend', this.template);
        this.setListener(w, d);
    }
    setListener(w, d) {
        w.addEventListener('hashchange', function() {
            var hash = w.location.hash;
            if (hash === "#about") {
                d.body.setAttribute('state', 'about');
                d.querySelector('#sideNav ul.nav a.item-about').classList.add('active');
                d.querySelector('#sideNav ul.nav a.item-db').classList.remove('active');
            } else if (hash === "#error") {
                d.body.setAttribute('state', 'error');
            } else if (hash === "#loading") {
                d.body.setAttribute('state', 'loading');
            } else {
                d.body.setAttribute('state', 'home');
                d.querySelector('#sideNav ul.nav a.item-about').classList.remove('active');
                d.querySelector('#sideNav ul.nav a.item-db').classList.add('active');
            }
        })
    }
}
export default Navigation;