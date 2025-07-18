import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectFormComponent } from './object-form.component';

describe('ObjectFormComponent', () => {
  let component: ObjectFormComponent;
  let fixture: ComponentFixture<ObjectFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
