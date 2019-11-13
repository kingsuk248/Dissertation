import Template from './template.js';
import './style/style.scss';
import About from './about.js';
import Header from './header.js';
import Dashboard from './dashboard.js';
import Navigation from './navigation.js';
import Service from './service';
import Donut from './donut.js';
import BarChart from './barchart';
import Card from './card';
import { setTimeout } from 'timers';

//const URL = "https://api.myjson.com/bins/9aqmw";
const URL = "http://localhost:9191/channels/parameters/allianz";

class Body {
    constructor() {
        this.template = `
        <div class="container-fluid">
            <div class="row">
                <nav id="sideNav" class="col-md-2 d-none d-md-block bg-light sidebar"></nav>
                <main class="main col-md-9 ml-sm-auto col-lg-10 px-4" id="main">
                    <header id="header"></header>
                    <div class="container-fluid">
                        <button type="button" class="btn btn-dark" id="update">Auto-Refresh Data</button>
                        <button type="button" class="btn btn-dark" id="stoprefresh">Stop Auto-Refresh</button>
                    </div>
                    
                    <section id="container" class="container-fluid"></section>
                </main>
            </div>
        </div>
        `;
        this.service = new Service({ donutId: "donut" });

    }
    inject(w, d, b) {
        b.setAttribute('state', 'loading');
        b.insertAdjacentHTML('beforeend', this.template);
        b.insertAdjacentHTML('beforeend', Template.errorTemplate);
        b.insertAdjacentHTML('beforeend', Template.loadingTemplate);
        (new Dashboard()).inject(d.getElementById('container'));
        (new Navigation()).inject(d.getElementById('sideNav'), w, d);
        (new Header()).inject(d.getElementById('header'));
        (new About()).inject(d.getElementById('container'));
        var that = this;
        setTimeout(() => {
            if (that.service) {
                that.service.start(URL);
                that.donutSubscriber = that.service.donutSub.subscribe(data => {
                    that.createDonut('donut', data);
                });
                that.donutObj = null;
                that.cardSubscriber = that.service.overviewCardSub.subscribe(data => {
                    that.addOverviewCards(data);
                });
                that.barchartObjects = {};
                that.barchartSubscriber = that.service.barchartSub.subscribe(data => {
                    that.createBarchart(data);
                });
            }

        }, 1000);
        var interval = null
        document.getElementById('update').addEventListener('click', e => {
            e.preventDefault();
            //that.update();
            interval = setInterval(function () { that.update(); }, 2000);
        });
        document.getElementById('stoprefresh').addEventListener('click', e => {
            e.preventDefault();
            clearInterval(interval)
        });
    }
    createDonut(id, dataset) {
        console.log('Donut dataset -> ', dataset);
        if (this.donutObj) {
            this.donutObj.update(dataset.datasets);
        } else {
            var options = {
                    responsive: true,
                    legend: {
                        position: 'right',
                    }
                },
                d = new Donut(document.getElementById(id), dataset, options);
            this.donutObj = d;
            window.donut = d;
            d.render();
        }
        //this.addOverviewCards(['clicks', 'leads', 'impressions']);
    }
    createBarchart(data) {
        var parent = document.getElementById('bar_chart_container');
        for (var k in data) {
            var props = data[k];
            props.options = {
                animation: {
                    duration: 200
                },
                scales: {
                    /*yAxes: [{
                        ticks: {
                            beginAtZero: true
                            }
                    }]*/
                    yAxes: [{
                        id: 'L',
                        type: 'linear',
                        position: 'left',
                        ticks: {
                          beginAtZero: true
                        }
                        },
                        {
                        id: 'R',
                        type: 'linear',
                        position: 'right',
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                    }
                }
            if (this.barchartObjects[props.id]) {
                this.barchartObjects[props.id].update(props.data);
            } else {
                var b = new BarChart(props);
                this.barchartObjects[props.id] = b;
                b.render(parent);
            }
        }
    }
    addOverviewCards(data) {
        console.log(data);
        for (var item in data) {
            console.log(item, data, data[item]);
            var el = document.querySelector('#card_container .card[name=' + item.toUpperCase() + ']');
            if (!el) {
                (new Card(data[item], item.toUpperCase()).inject(document.getElementById('card_container')));
            } else {
                el.querySelector('.card-title').innerHTML = data[item];
                el.querySelector('.card-text').innerHTML = item.toUpperCase();
            }
        }
    }
    update() {
        if (this.service) {
            //this.service.start('https://api.myjson.com/bins/dtxpo');
            this.service.start(URL);
        }
    }
}
console.log('Inside main.js');
let b = new Body();
b.inject(window, document, document.body);
